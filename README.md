#TFL Tube Departures 
[![Circle CI](https://circleci.com/gh/Numan1617/TFLTubeDepartures/tree/develop.svg?style=shield)](https://circleci.com/gh/Numan1617/TFLTubeDepartures/tree/develop)

##Project setup
###Retrolambda
For retrolambda to operate you will need to [download](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and install JDK 8.

You will then need to add a gradle property called `JAVA8_HOME` that points to the location of your JDK installations to either the projects gradle.properties or the global gradle.properties for the user. The default location to place this is `USER_HOME/.gradle`, however it may be customised on your machine with the `GRADLE_USER_HOME` environment variable.

Example for Mac:

```groovy
JAVA8_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_XX.jdk/Contents/Home
```

###API Setup
To configure your TFL API keys with the app you will need to enter them in the respective fields in `app/tflApiKeys.gradle`. 

If you don't already have API keys and an App ID you can simply [sign up](https://api-portal.tfl.gov.uk/signup) for the TFL unified API for free.


##Git
This project uses Git flow and as a result we have the branches:

- `master` - Stable at all times and contains the latest release
- `develop` - Stable at all times and contains the latest complete feature
- `release/[release name]` - A branch from develop for final checks / testing before being merged to master and released
- `feature/[feature name]` - Used for user stories or bug fixes
- `hotfix/[hotifx name]` - Used for critical bugs that need to be applied to all branches
