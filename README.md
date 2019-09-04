
# react-native-receive-shared-data

## Getting started

`$ npm install react-native-receive-shared-data --save`

### Mostly automatic installation

`$ react-native link react-native-receive-shared-data`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import package com.ejaz.RNReceiveSharedData.RNReceiveSharedDataPackage;` to the imports at the top of the file
  - Add `new RNReceiveSharedDataPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-receive-shared-data'
  	project(':react-native-receive-shared-data').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-receive-shared-data/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-receive-shared-data')
  	```


## Usage
```javascript
import RNReceiveSharedData from 'react-native-receive-shared-data';

// TODO
```
  