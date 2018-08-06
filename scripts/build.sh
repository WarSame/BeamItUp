#!/usr/bin/env bash
buildType='debug'

#clean
chmod +x gradlew
./gradlew clean --stacktrace

# build
if [ $buildType = 'debug' ]; then
	./gradlew assembleDebug --stacktrace
elif [ $buildType = 'release' ]; then
	./gradlew assembleRelease --stacktrace
fi

#test
if [ $buildType = 'debug' ]; then
    ./gradlew testDebugUnitTest --stacktrace
elif [ $buildType = 'release' ]; then
    ./gradlew testReleaseUnitTest --stacktrace
fi

#report
mkdir report/test-results
modules=("app" "common")
for module in "${modules[@]}"
do

    testsDir=""
    if [ $buildType = 'debug' ]; then
        testsDir="$module/build/test-results/testDebugUnitTest"
    elif [ $buildType = 'release' ]; then
        testsDir="$module/build/test-results/testReleaseUnitTest"
    fi

    if [ ! "$(ls -A $testsDir)" ]; then
        echo "Unit tests report wasn't found for module: $module"
        continue
    fi

    # copy all files inside, to our folder
    cp $testsDir/* report/test-results/

done