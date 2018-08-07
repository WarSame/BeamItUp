
echo "here"

rem clean
.\gradlew clean --stacktrace

rem build
.\gradlew assembleDebug --stacktrace

rem test
.\gradlew testDebugUnitTest --stacktrace

rem report
mkdir report\test-results
