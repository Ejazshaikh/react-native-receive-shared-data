
# react-native-receive-shared-data

## [Android Only]

A react native library to receive shared data in the form of text, image or multiple images from other apps tp your app.

## Installation

Step 1: Install the library
```
npm install react-native-receive-shared-data --save
```

Step2: Link the library (not needed for react-native >= 0.60.0)
```
react-native link react-native-receive-shared-data
```

Step3: Update your App Manifest

```
<activity android:name=".ui.MyActivity" >
    <!--If you want support for Receiving Image-->
    <intent-filter>
        <action android:name="android.intent.action.SEND" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:mimeType="image/*" />
    </intent-filter>
    <!--If you want support for Receiving Text-->
    <intent-filter>
        <action android:name="android.intent.action.SEND" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:mimeType="text/plain" />
    </intent-filter>
    <!--If you want support for Receiving multiple Images-->
    <intent-filter>
        <action android:name="android.intent.action.SEND_MULTIPLE" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:mimeType="image/*" />
    </intent-filter>
</activity>
```



## Usage
```javascript
import React, { Component } from 'react';
import RNReceiveSharedData from 'react-native-receive-shared-data';

class App extends Component {
  componentDidMount() {
    this.checkForSharedData();
  }

  async checkForSharedData() {
    const data = await RNReceiveSharedData.getSharedData();
    if (data) {
      if (data.type === 'TEXT') {
        // Things you want to do...
      } else if (data.type === 'IMAGE') {
        // Things you want to do...
      } else if (data.type === 'IMAGES') {
        // Things you want to do...
      }
    }
  }
 }
```
  
