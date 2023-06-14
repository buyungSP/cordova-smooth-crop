errorResult = [
    { 101: "You need to pass a valid image path in the 'url' key of the options object" },
    { 102: "You have not provided a valid format for the 'ratio' key" },
    { 201: "You should pass an array of image paths" },
    { 202: "The image array should not be empty" },
]
module.exports = {
    open: function(options, successCallback, errorCallback) {
        finalOption = {
            url: "",
            ratioX: 1,
            ratioY: 1,
            reqWidth: 0,
            reqHeight: 0,
            cropShape: 0,
            guideline: 0,
            scaleType: 0,
            outputCompressQuality: 90,
            showCropOverlay: true,
            multiTouchEnabled: false,
            autoZoomEnabled: false,
            setFixAspectRatio: false,
            allowRotation: false,
            allowFlipping: false,
            title: "Image Cropper"
        };

        if (!options.url) {
            errorCallback(errorResult[0]);
            return;
        } else {
            finalOption.url = options.url;
            if(options.setFixAspectRatio){
                finalOption.setFixAspectRatio = options.setFixAspectRatio;
            }else{
                if (options.ratio) {
                    var ratioArr = options.ratio.split("/");
                    if (ratioArr.length > 1) {
                        finalOption.ratioX = parseInt(ratioArr[0]);
                        finalOption.ratioY = parseInt(ratioArr[1]);
                    }
                } else {
                    errorCallback(errorResult[1]);
                    return;
                }
            }

            if (options.size) {
                var sizeArr = options.size.split("x");
                if (sizeArr.length > 1) {
                    finalOption.reqWidth = parseInt(sizeArr[0]);
                    finalOption.reqHeight = parseInt(sizeArr[1]);
                }
            }
            finalOption.cropShape = finalOption.cropShape || options.cropShape;
            finalOption.guideline = finalOption.guideline || options.guideline;
            finalOption.scaleType = finalOption.scaleType || options.scaleType;
            finalOption.outputCompressQuality = finalOption.outputCompressQuality || options.outputCompressQuality;

            finalOption.showCropOverlay = finalOption.showCropOverlay || options.showCropOverlay;
            finalOption.multiTouchEnabled = finalOption.multiTouchEnabled || options.multiTouchEnabled;
            finalOption.autoZoomEnabled = finalOption.autoZoomEnabled || options.autoZoomEnabled;
            finalOption.allowRotation = finalOption.allowRotation || options.allowRotation;
            finalOption.allowFlipping = finalOption.allowFlipping || options.allowFlipping;
            finalOption.title = finalOption.title || options.title;
        }
        cordova.exec(successCallback, errorCallback, "Crop", "open", [finalOption]);
    },
    getImageDimension: function(imageArr, successCallback, errorCallback) {
        if (!Array.isArray(imageArr)) {
            errorCallback(errorResult[2]);
            return;
        }

        if (imageArr.length < 1) {
            errorCallback(errorResult[3]);
            return;
        }

        cordova.exec(successCallback, errorCallback, "Crop", "getImageDimension", [imageArr]);
    }
};