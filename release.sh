#!/bin/bash

# Maven Release Script
# This script replicates the GitHub Actions workflow for releasing to Maven Central

# Display usage information
usage() {
  echo "Usage: $0 -r RELEASE_VERSION -d DEVELOPMENT_VERSION -u MAVEN_USERNAME -p MAVEN_PASSWORD -k GPG_PRIVATE_KEY -i GPG_KEY_ID -s GPG_PASSPHRASE [-g GITHUB_TOKEN]"
  echo ""
  echo "Arguments:"
  echo "  -r RELEASE_VERSION       Release version (e.g., 0.1.0)"
  echo "  -d DEVELOPMENT_VERSION   Next development version with -SNAPSHOT suffix (e.g., 0.2.0-SNAPSHOT)"
  echo "  -u MAVEN_USERNAME        Maven Central (Sonatype) username"
  echo "  -p MAVEN_PASSWORD        Maven Central (Sonatype) password"
  echo "  -k GPG_PRIVATE_KEY       GPG private key (exported with gpg --export-secret-keys --armor KEY_ID)"
  echo "  -i GPG_KEY_ID            GPG key ID"
  echo "  -s GPG_PASSPHRASE        GPG passphrase"
  echo "  -g GITHUB_TOKEN          GitHub token (optional, for creating GitHub releases)"
  echo ""
  echo "Example:"
  echo "  $0 -r 0.1.0 -d 0.2.0-SNAPSHOT -u myusername -p mypassword -k \"$(cat my_private_key.asc)\" -i A1B2C3D4 -s mypassphrase"
  exit 1
}

# Parse command line arguments
while getopts "r:d:u:p:k:i:s:g:" opt; do
  case $opt in
    r) RELEASE_VERSION="$OPTARG" ;;
    d) DEVELOPMENT_VERSION="$OPTARG" ;;
    u) MAVEN_USERNAME="$OPTARG" ;;
    p) MAVEN_PASSWORD="$OPTARG" ;;
    k) GPG_PRIVATE_KEY="$OPTARG" ;;
    i) GPG_KEY_ID="$OPTARG" ;;
    s) GPG_PASSPHRASE="$OPTARG" ;;
    g) GITHUB_TOKEN="$OPTARG" ;;
    *) usage ;;
  esac
done

# Check required arguments
if [ -z "$RELEASE_VERSION" ] || [ -z "$DEVELOPMENT_VERSION" ] || [ -z "$MAVEN_USERNAME" ] || 
   [ -z "$MAVEN_PASSWORD" ] || [ -z "$GPG_PRIVATE_KEY" ] || [ -z "$GPG_KEY_ID" ] || [ -z "$GPG_PASSPHRASE" ]; then
  echo "Error: Missing required arguments"
  usage
fi

# Set environment variables for Maven
export MAVEN_USERNAME="$MAVEN_USERNAME"
export MAVEN_PASSWORD="$MAVEN_PASSWORD"
export MAVEN_GPG_PASSPHRASE="$GPG_PASSPHRASE"
export MAVEN_GPG_PRIVATE_KEY="$GPG_PRIVATE_KEY"
export MAVEN_GPG_KEY_ID="$GPG_KEY_ID"

# Import GPG key
echo "$GPG_PRIVATE_KEY" | gpg --batch --import

# Configure Git user
git config user.email "release-script@example.com"
git config user.name "Release Script"

echo "Starting release process for version $RELEASE_VERSION"

# Update to release version
echo "Updating to release version $RELEASE_VERSION"
mvn --settings .mvn/settings.xml versions:set -DnewVersion="$RELEASE_VERSION"
if [ $? -ne 0 ]; then
  echo "Error: Failed to update to release version"
  exit 1
fi

# Build and deploy to Maven Central
echo "Building and deploying to Maven Central"
mvn --settings .mvn/settings.xml clean deploy -P release -DskipTests
if [ $? -ne 0 ]; then
  echo "Error: Failed to build and deploy to Maven Central"
  exit 1
fi

# Create Git tag
echo "Creating Git tag v$RELEASE_VERSION"
git add pom.xml
git commit -m "Release version $RELEASE_VERSION"
git tag -a "v$RELEASE_VERSION" -m "Release version $RELEASE_VERSION"
if [ $? -ne 0 ]; then
  echo "Error: Failed to create Git tag"
  exit 1
fi

# Update to next development version
echo "Updating to next development version $DEVELOPMENT_VERSION"
mvn --settings .mvn/settings.xml versions:set -DnewVersion="$DEVELOPMENT_VERSION"
if [ $? -ne 0 ]; then
  echo "Error: Failed to update to next development version"
  exit 1
fi

# Commit next development version
echo "Committing next development version"
git add pom.xml
git commit -m "Prepare for next development iteration $DEVELOPMENT_VERSION"
if [ $? -ne 0 ]; then
  echo "Error: Failed to commit next development version"
  exit 1
fi

# Push release version to main and tags
echo "Pushing release version to main and tags"
git push origin main
git push origin "v$RELEASE_VERSION"
if [ $? -ne 0 ]; then
  echo "Error: Failed to push release version to main and tags"
  exit 1
fi

# Handle develop branch
echo "Handling develop branch"
# Save the development version commit hash and content
DEV_COMMIT=$(git rev-parse HEAD)
git show "$DEV_COMMIT:pom.xml" > /tmp/dev-pom.xml

# Check if develop branch exists, create it if it doesn't
if ! git ls-remote --heads origin develop | grep develop; then
  echo "Creating develop branch"
  # Create develop branch from the current state (after committing development version)
  git checkout -b develop
else
  echo "Updating existing develop branch"
  # If develop branch exists, fetch it and checkout
  git fetch origin develop
  git checkout develop

  # Apply the development version changes
  cp /tmp/dev-pom.xml pom.xml
  git add pom.xml
  git commit -m "Prepare for next development iteration $DEVELOPMENT_VERSION"
fi

# Push the development version to develop branch
git push origin develop
if [ $? -ne 0 ]; then
  echo "Error: Failed to push development version to develop branch"
  exit 1
fi

# Return to main branch
git checkout main

# Create GitHub Release if GitHub token is provided
if [ -n "$GITHUB_TOKEN" ]; then
  echo "Creating GitHub Release"
  
  # Get the repository name from the remote URL
  REPO_URL=$(git config --get remote.origin.url)
  REPO_NAME=$(echo "$REPO_URL" | sed -e 's/.*github.com[:\/]\(.*\)\.git/\1/')
  
  # Create GitHub release using GitHub API
  curl -s -X POST \
    -H "Authorization: token $GITHUB_TOKEN" \
    -H "Accept: application/vnd.github.v3+json" \
    "https://api.github.com/repos/$REPO_NAME/releases" \
    -d "{
      \"tag_name\": \"v$RELEASE_VERSION\",
      \"name\": \"Release $RELEASE_VERSION\",
      \"draft\": false,
      \"prerelease\": false,
      \"generate_release_notes\": true
    }"
  
  if [ $? -ne 0 ]; then
    echo "Warning: Failed to create GitHub Release"
  else
    echo "GitHub Release created successfully"
  fi
fi

echo "Release process completed successfully!"
echo "Released version: $RELEASE_VERSION"
echo "Next development version: $DEVELOPMENT_VERSION"