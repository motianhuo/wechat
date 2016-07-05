#!/bin/bash
#change package name from com.easemob... to com.hyphenate...
#build ant need env var ANDROID_HOME, please change it to according your enviroment.



# migrate sdk
(
    echo "list files..."
    #filelist=`find res -type f -name "*.xml"`
    #filelist+=`find src -type f -name "*.java"`
    #filelist+= AndroidManifest.xml
    filelist=`grep -l easemob  * -r | grep -v hyphenate.sh`

    for file in $filelist; do
        echo $file
        gsed -i 's/easemob/hyphenate/g' $file
    done

    mv src/com/easemob src/com/hyphenate
    mv simpledemo/src/com/easemob simpledemo/src/com/hyphenate
)


