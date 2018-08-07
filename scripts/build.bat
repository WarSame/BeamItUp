
echo "here"

#clean
.\gradlew clean --stacktrace

# build
.\gradlew assembleDebug --stacktrace

#test
.\gradlew testDebugUnitTest --stacktrace

#report
mkdir report\test-results
