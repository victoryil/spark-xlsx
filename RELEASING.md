# Releasing to Maven Central

This document provides detailed instructions for project maintainers on how to release new versions of the Spark XLSX library to Maven Central via Sonatype OSSRH.

## Prerequisites

Before you can release to Maven Central, you need:

1. A Sonatype JIRA account (create one at https://issues.sonatype.org/secure/Signup!default.jspa)
2. Access to the `dev.victoryil` group ID in Sonatype OSSRH
3. GPG keys for signing the artifacts
4. Maven installed on your system

## One-time Setup

### 1. Create and Configure GPG Keys

If you don't have GPG keys yet:

```bash
# Generate a new key pair
gpg --gen-key

# List your keys to get the key ID
gpg --list-keys

# Distribute your public key to a key server
gpg --keyserver hkp://pool.sks-keyservers.net --send-keys YOUR_KEY_ID
```

### 2. Configure Maven Settings

Create or update your `~/.m2/settings.xml` file with your Sonatype credentials:

```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>your-sonatype-username</username>
      <password>your-sonatype-password</password>
    </server>
  </servers>

  <profiles>
    <profile>
      <id>ossrh</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <gpg.keyname>YOUR_GPG_KEY_ID</gpg.keyname>
        <!-- Optional: specify passphrase if not using gpg-agent -->
        <!-- <gpg.passphrase>your-gpg-passphrase</gpg.passphrase> -->
      </properties>
    </profile>
  </profiles>
</settings>
```

A template for this file is available in `.mvn/settings.xml`.

## Release Process

You can release to Maven Central either manually or using GitHub Actions.

## Option 1: Automated Release with GitHub Actions

### 1. Set Up GitHub Secrets

Before using the GitHub Actions workflow, you need to set up the following secrets in your GitHub repository:

1. Go to your GitHub repository
2. Navigate to "Settings" > "Secrets and variables" > "Actions"
3. Add the following secrets:
   - `OSSRH_USERNAME`: Your Sonatype OSSRH username
   - `OSSRH_PASSWORD`: Your Sonatype OSSRH password
   - `GPG_PRIVATE_KEY`: Your GPG private key (export it with `gpg --export-secret-keys --armor YOUR_KEY_ID`)
   - `GPG_PASSPHRASE`: The passphrase for your GPG key

### 2. Trigger the Release Workflow

1. Go to your GitHub repository
2. Navigate to "Actions" > "Maven Release"
3. Click "Run workflow"
4. Enter the following information:
   - Release version (e.g., `0.1.0`)
   - Next development version (e.g., `0.2.0-SNAPSHOT`)
5. Click "Run workflow"

The workflow will:
1. Update the version to the release version
2. Build and deploy to Maven Central
3. Create a Git tag and GitHub release
4. Update the version to the next development version
5. Push all changes back to the repository

### 3. Monitor the Release

1. Check the workflow progress in the Actions tab
2. Verify the release in Sonatype OSSRH at https://s01.oss.sonatype.org/
3. The artifacts should appear in Maven Central within a few hours

## Option 2: Manual Release

### 1. Prepare for Release

1. Update the version in `pom.xml` (remove `-SNAPSHOT` suffix for a release)
2. Update the CHANGELOG.md file with the new version's changes
3. Commit all changes and push to the repository

### 2. Deploy a Snapshot (Optional)

If you want to test the deployment process with a snapshot version:

```bash
mvn clean deploy
```

This will deploy the artifact to the Sonatype snapshot repository.

### 3. Deploy a Release

To deploy a release version:

```bash
mvn clean deploy -P release
```

This command will:
1. Compile the code
2. Run tests
3. Generate Javadocs and source JARs
4. Sign all artifacts with GPG
5. Deploy to Sonatype OSSRH
6. Automatically release to Maven Central (due to `autoReleaseAfterClose=true` in the nexus-staging-maven-plugin configuration)

### 4. Verify the Release

1. Check the Sonatype repository manager at https://s01.oss.sonatype.org/
2. Verify that your artifacts have been properly released
3. The artifacts should appear in Maven Central within a few hours

### 5. Post-Release Tasks

1. Update the version in `pom.xml` to the next development version (with `-SNAPSHOT` suffix)
2. Commit and push these changes
3. Create a new release/tag in GitHub with release notes

## Troubleshooting

### GPG Issues

If you encounter GPG-related issues:

```bash
# Ensure your GPG key is available
gpg --list-keys

# If using gpg-agent, ensure it's running
gpg-connect-agent /bye

# Test signing
echo "test" | gpg --clearsign
```

### Nexus Staging Issues

If there are issues with the Nexus staging process:

1. Log in to https://s01.oss.sonatype.org/
2. Go to "Staging Repositories"
3. Find your repository and check for any rule violations
4. If needed, you can manually drop or release the repository

## References

- [Sonatype OSSRH Guide](https://central.sonatype.org/publish/publish-guide/)
- [GPG Setup Guide](https://central.sonatype.org/publish/requirements/gpg/)
- [Maven Deployment Guide](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [GitHub Actions Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [GitHub Actions for Java with Maven](https://docs.github.com/en/actions/automating-builds-and-tests/building-and-testing-java-with-maven)
