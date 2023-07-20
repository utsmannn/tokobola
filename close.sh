#!/bin/bash

current_dir=$(pwd)

osascript -e 'tell application "Android Studio" to if it is running then quit'
osascript -e "tell app \"Terminal\"
    do script \"cd $current_dir && ./cleanup-open.sh\"
end tell"

osascript -e 'tell app "Android Studio"'