# 3dPhotoMaker

Application to support creation 3D photos. Supporting formats:
  - .png - represents image of anaglyph (in project)
  - .pns - its png stereo format, it consists of a side-by-side image

Application works with OpenCV library, its used for detect optical flow and operation on images. Detecting optical flow is used to automation making second image. Last step is mergeing two images in one output image by two diffrent method: [pns](https://raw.githubusercontent.com/mk307009/3dPhotoMaker/master/screenshot/pns.png) and [anaglyph](https://raw.githubusercontent.com/mk307009/3dPhotoMaker/master/screenshot/anaglyph.png).

### Presentation of application
![alt text](https://raw.githubusercontent.com/mk307009/3dPhotoMaker/master/screenshot/menu.png "Opened menus")

First photo presents menus. The second image (below) shows state of the application while detecting optical flow.

![alt text](https://raw.githubusercontent.com/mk307009/3dPhotoMaker/master/screenshot/move.png "Making second photo")

##### Example photos
![alt text](https://raw.githubusercontent.com/mk307009/3dPhotoMaker/master/screenshot/anaglyph.png "Anaglyph photo")
![alt text](https://raw.githubusercontent.com/mk307009/3dPhotoMaker/master/screenshot/pns.png "PNS photo")

### Version
0.8.1
