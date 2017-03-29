# Swipper
Android Library for custom views to control brightness , volume and seek through swipable gestures . These views could easily replace the conventional volume / brightness / seek controls that we have in music player ,video player or gallery apps .

<img src="https://raw.githubusercontent.com/pkarira/Swipper/19ec63a1f3833d8d12b21941bdf8bcd7fe8b62c0/library/src/main/res/drawable/finalfinal2.gif" width="250" height="400">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://raw.githubusercontent.com/pkarira/Swipper/19ec63a1f3833d8d12b21941bdf8bcd7fe8b62c0/library/src/main/res/drawable/finalfinal1.gif" width="250" height="400"><br><br>
<h3>Dependency</h3>
Add dependency to build.gradle of your app

```java
 dependencies
 {
 compile 'com.swipper.library:library:0.1.1'
 }
 ```

<h3>Usage</h3>
 
 Extend the activity in which you want to implement by Swipper instead of Activity / AppCompatActivity like:
 
 ```java
 public class MainActivity extends Swipper{}
 ```
 Pass the context of your activity in set(context) method in onCreate() of your activity like :
 ```java
 set(this)
 ```
 Now select swipe options for Brightness/Volume/SeekTo functions , to do this call Brightness() , Volume() , Seek() methods with orientation as parameter in onCreate() of your activity , suppose you want brightness on vertical swipe then do this:
 ```java
 Brightness(Orientation.VERTICAL)
 ```
 Orientation can also be set to HORIZONTAL for working on horizontal swipe or to CIRCULAR for circular seek bar .
 Similarily do this for volume too .
 ```java
 Volume(Orientation.CIRCULAR)
 ```
 <h4>Circular custom view appears on double tapping the screen</h4>
 
In seek method you need to pass your VideoView / Mediaplayer object along with orientation parameter , like:

```java
 MediaPlayer mediaPlayer=.....
 Seek(Orientation.HORIZONTAL,mediaPlayer)
 ```

 
  <h3>Orientations</h3>
 
| Orientation        | Description         | 
| ------------- |:-------------:| 
| Orientation.VERTICAL      | Associates Brightness/Volume/Seek option on vertical gesture i.e up and down swipe| 
| Orientation.HORIZONTAL     |Associates Brightness/Volume/Seek option on horizontal gesture i.e left and right swipe      | 
| Orientation.CIRCULAR| Associates Brightness or Volume option on circular seek bar | 
 
 <h3>Other Methods</h3>
 
| Method        | Role          | 
| ------------- |:-------------:| 
| setColor(String color)      | pass hex color to change color of custom view which is by default a shade of orange | 
| disableBrightness()     |to stop working of view associated with brightness      | 
| disableSeek() | to stop working of view associated with Seek      | 
|disableVolume() | to stop working of view associated with Volume| 
| enableBrightness()| to again start the working of view associated with Brightness| 
| enableSeek()|to again start the working of view associated with Seek | 
| enableVolume()| to again start the working of view associated with Volume | 
<h3> Library is completely exclusive and no external library has been used in making it </h3>

   <h3>License</h3>
  
  * [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
```
Copyright 2016 Pulkit Karira

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
