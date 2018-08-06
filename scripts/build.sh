buildType='debug'

if [ $buildType = 'debug' ]; then
    ./gradlew testDebugUnitTest --stacktrace
elif [ $buildType = 'release' ]; then
    ./gradlew testReleaseUnitTest --stacktrace
fi

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