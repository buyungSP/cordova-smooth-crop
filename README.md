# Crop Smooth Plugin

Crop Smooth Plugin is a Cordova plugin that is used to crop and resize images in Cordova applications. This plugin provides functions to open the image crop screen with configured options and get image dimensions from the given image array.

# Platform Support
- Android

# Installation
You can install this plugin through Cordova CLI by running the following command:

> cordova plugin add cordova-smooth-crop

# Usage
This plugin provides two functions, "open" and "getImageDimension". Here is an example of how to use them:

### Function "open"

> cordova.smooth.crop.open({
>     url: 'file:///path/to/image.jpg', // (string): path gambar yang akan dipotong. Contoh: file:///path/to/image.jpg
>     ratio: '1/1', // (string): rasio aspek gambar yang akan dipotong. Contoh: '1/1'
>     setFixAspectRatio: true, // setFixAspectRatio (boolean): mengaktifkan atau menonaktifkan rasio aspek gambar yang tetap saat memotong gambar. Contoh: false, jika nilai true ratio akan menjadi '1/1'
>     size: '300x300' // (string): ukuran gambar yang akan dipotong. Contoh: '300x300'
>     outputCompressQuality: 90, // (number): kualitas kompresi gambar yang dihasilkan. Contoh: 90
>     cropShape: 0, // (number): bentuk potongan gambar. Contoh: 0 (RECTANGLE) [RECTANGLE,OVAL]
>     guideline: 0, // (number): tampilan panduan saat memotong gambar. Contoh: 1 (ON_TOUCH) [OFF,ON_TOUCH,ON]
>     scaleType: 0, // (number): tipe skala gambar saat memotong. Contoh: 0 (FIT_CENTER) [FIT_CENTER,CENTER,CENTER_CROP,CENTER_INSIDE]
>     showCropOverlay: true, // (boolean): menampilkan atau menyembunyikan overlay saat memotong gambar. Contoh: true
>     multiTouchEnabled: true, // (boolean): mengaktifkan atau menonaktifkan multitouch saat memotong gambar. Contoh: false
>     autoZoomEnabled: false, // (boolean): mengaktifkan atau menonaktifkan auto zoom saat memotong gambar. Contoh: false
>     allowRotation: true, // (boolean): mengaktifkan atau menonaktifkan rotasi gambar saat memotong gambar. Contoh: false
>     allowFlipping: true, // (boolean): mengaktifkan atau menonaktifkan flipping gambar saat memotong gambar. Contoh: false
>     title: "Image Cropper", // (string): judul layar potong gambar. Contoh: 'Image Cropper'
> }, function success(result) {
>     console.log(result); // {width:'',height:'',imgPath:'',base64:''}
> }, function error(err) {
>     console.error(err); // {code:'',message:''}
> });

The "open" function is used to open the image crop screen with configured options. Available options are:

- url (string): the path of the image to be cropped.
- ratio (string): the aspect ratio of the image to be cropped.
- setFixAspectRatio (boolean): enable or disable the fixed aspect ratio of the image when cropping the image.
- size (string): the size of the image to be cropped.
- outputCompressQuality (number): the compression quality of the resulting image.
- cropShape (number): the shape of the cropped image.
- guideline (number): the guideline display when cropping the image.
- scaleType (number): the scale type of the image when cropping.
- showCropOverlay (boolean): show or hide the overlay when cropping the image.
- multiTouchEnabled (boolean): enable or disable multitouch when cropping the image.
- autoZoomEnabled (boolean): enable or disable auto zoom when cropping the image.
- allowRotation (boolean): enable or disable image rotation when cropping the image.
- allowFlipping (boolean): enable or disable image flipping when cropping the image.
- title (string): the title of the image crop screen.


### Function "getImageDimension"

> cordova.smooth.crop.getImageDimension(['file:///path/to/image1.jpg', 'file:///path/to/image2.jpg'], function success(result) {
>     console.log(result); // {width:'',height:'',imgPath:'',base64:''}
> }, function error(err) {
>     console.error(err); // {code:'',message:''}
> });

The "getImageDimension" function is used to get image dimensions from the given image array. You must provide an array of image paths as the first argument.

# Conclusion
Crop Smooth Plugin can help simplify the use of image cropping functions in Cordova applications running on the Android platform.