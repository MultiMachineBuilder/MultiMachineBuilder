## Eclipse plugins
* SonarLint
* WindowBuilder
* ResourceBundle editor
* [Eclipse Maven integration (M2E extension) for null analysis (OPEN THIS IN THE BROWSER, DON'T COPY AND PASTE INTO ECLLIPSE)](https://github.com/lastnpe/eclipse-external-annotations-m2e-plugin)

## IDE setup
0. Import the project (#How to contribute)
1. Download and install Eclipse with JDT
2. Install listed plugins
3. Create a Personal Access Token for your account with only full repository access.
4. Provide your username, and PAT instead of a password.
Store the credentials in Secure Store, because PAT won't appear ever again
5. Access SonarCloud using GitHub
6. Create a SonarCloud token and use it in the SonarLint
7. Sync SonarLint to the MultiMachineBuilder project

## How to contribute
1. Fork the project and create a draft pull request from your fork to the main repo.
  <br> Monniasza should work directly on master
  <br> All other collaborators should work on their forks or side branches and submit PRs
  <br> All other contributors should work on their forks
2. Import your fork into Eclipse (collaborators should import the main repo)
3. Pull the desired branch (usually `master`) using Eclipse.
4. Write some code, translate the game, or create assets in Eclipse
5. Test your changes
6. Commit small changes often
7. When finished work, convert PR to ready to review
8. Discuss changes and adjust them
9. When you agree with Monniasza, request a merge
10. Monniasza merges your changes
11. Your changes are integrated with the game

# By type

## New functionality
* New functionality must have a separate issue
* Be tracked with a milestone
* Must have 'development' label
* Must be assigned
* Must be located in a 'Future plans' project.

## Bug reports
* Bugs must be reporteds using an issue template
* All fields must be filled out properly

## Bug fixes
* Bug fixes must be marked with a 'bug' label
* Bug fixes must be marked to close an associated bug report

## Pull requests
* Pull requests must meet coding guidelines outlined in the SonarCloud project

## Feature requests
* Feature requests may be backed by a discussion (or converted from it), or may be created using an issue template
* Feature requests must have described new functionality
* Alternatives must be described as well

## Security vulnerabilities
* Do not report vulnerabilities publicly until a fix is underway, or workaround is developed, or 90 days elapsed since discovery
