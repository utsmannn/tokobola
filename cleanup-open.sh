#!/bin/bash
rm -rf .idea
./gradlew clean
rm -rf .gradle
rm -rf build
rm -rf */build
rm -rf iosApp/iosApp.xcworkspace
rm -rf iosApp/Pods
rm -rf iosApp/iosApp.xcodeproj/project.xcworkspace
rm -rf iosApp/iosApp.xcodeproj/xcuserdata

echo -en "> Reopen Android Studio when not running on 5 second.."
sleep 2
echo -en "\r> Reopen Android Studio when not running on 4 second.."
sleep 2
echo -en "\r> Reopen Android Studio when not running on 3 second.."
sleep 2
echo -en "\r> Reopen Android Studio when not running on 2 second.."
sleep 2
echo -en "\r> Reopen Android Studio when not running on 1 second.."
sleep 2

open -a /Applications/Android\ Studio.app