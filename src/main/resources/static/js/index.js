var currentImage = $('#current-image');
var imageInput = $('#image-input');
var colorSelector = $('#color-select');
var colorDifferenceSelector = $('#color-difference');
var colorSearchRadiusSelector = $('#color-search-radius');
var colorEntireButton = $('#color-entire-submit');
var colorAreaButton = $('#color-area-submit');
var colorPointButton = $('#color-point-submit');
var perspectiveButton = $('#perspective-submit');
var outputArea = $('#output');

var imageId = 0;
var imageIsUploaded = false;
var selectedPoints = [];

const IMAGE_SRC_PREFIX = 'data:image/png;base64,';

$.ajaxSetup({
    contentType: 'application/json; charset=UTF-8'
});

imageInput.change(imageLoadHandler);
colorEntireButton.click(colorEntireHandler);
colorAreaButton.click(colorAreaHandler);
colorPointButton.click(colorPointHandler);
currentImage.click(selectPointHandler);
perspectiveButton.click(perspectiveHandler);

function imageLoadHandler(e) {
    const files = e.target.files;
    if (files.length > 0) loadFile(e.target.files[0]);
}

function colorEntireHandler() {
    const request = (id) => apiColorEntire(
        colorSelector.val(),
        parseInt(colorDifferenceSelector.val()),
        id,
        (xhr) => failColorRequestHandler(xhr, request)
    );

    if (!imageIsUploaded) apiImage(getBase64FromImg(currentImage), request);
    else request(imageId);
}

function colorAreaHandler() {
    const l = selectedPoints.length;
    if (l < 2) return;

    const request = (id) => apiColorArea(
        colorSelector.val(),
        parseInt(colorDifferenceSelector.val()),
        id,
        selectedPoints[l - 2][0],
        selectedPoints[l - 2][1],
        selectedPoints[l - 1][0],
        selectedPoints[l - 1][1],
        (xhr) => failColorRequestHandler(xhr, request)
    );

    if (!imageIsUploaded) apiImage(getBase64FromImg(currentImage), request);
    else request(imageId);
}

function colorPointHandler() {
    const l = selectedPoints.length;
    if (l < 1) return;

    const request = (id) => apiColorPoint(
        id,
        selectedPoints[l - 1][0],
        selectedPoints[l - 1][1],
        parseInt(colorDifferenceSelector.val()),
        parseInt(colorSearchRadiusSelector.val()),
        (xhr) => failColorRequestHandler(xhr, request)
    );

    if (!imageIsUploaded) apiImage(getBase64FromImg(currentImage), request);
    else request(imageId);
}

function failColorRequestHandler(xhr, repeatRequestCallback) {
    if (xhr === undefined || xhr.responseJSON === undefined || xhr.responseJSON.message === undefined) return;
    if (xhr.responseJSON.message.indexOf('Изображение отсутствует в кэше') !== -1) {
        apiImage(getBase64FromImg(currentImage), repeatRequestCallback);
    }
}

function perspectiveHandler() {
    const l = selectedPoints.length;
    if (l < 2) return;
    apiPerspective(selectedPoints, getBase64FromImg(currentImage), 500, 500);
}

function selectPointHandler(e) {
    const target = e.target;
    const posX = $(this).offset().left;
    const posY = $(this).offset().top;
    let x = Math.round((e.pageX - posX) * (target.naturalWidth / target.clientWidth));
    let y = Math.round((e.pageY - posY) * (target.naturalHeight / target.clientHeight));
    selectedPoints.push([x,y]);
    if (selectedPoints.length > 4) selectedPoints.shift();
    console.log(selectedPoints);
}

function apiImage(base64, callback) {
    const json = JSON.stringify({ image: base64 });
    $.post('/api/image', json, (result) => {
        imageIsUploaded = true;
        imageId = result.imageId;
        callback(result.imageId);
    });
}

function apiColorEntire(color, colorDifference, id, onfail) {
    const json = JSON.stringify({
        color: {
            red: parseInt(color.substr(1, 2), 16),
            green: parseInt(color.substr(3, 2), 16),
            blue: parseInt(color.substr(5, 2), 16),
        },
        colorDifference: colorDifference,
        imageId: id
    });

    $.post('/api/color/entire', json, (result) => outputArea.first().text(JSON.stringify(result.pixels)))
        .fail(onfail);
}

function apiColorArea(color, colorDifference, id, minX, minY, maxX, maxY, onfail) {
    const json = JSON.stringify({
        color: {
            red: parseInt(color.substr(1, 2), 16),
            green: parseInt(color.substr(3, 2), 16),
            blue: parseInt(color.substr(5, 2), 16),
        },
        colorDifference: colorDifference,
        minX: minX,
        minY: minY,
        maxX: maxX,
        maxY: maxY,
        imageId: id
    });

    $.post('/api/color/area', json, (result) => outputArea.first().text(JSON.stringify(result.pixels)))
        .fail(onfail);
}

function apiColorPoint(id, x, y, colorDifference, radius, onfail) {
    const json = JSON.stringify({
        x: x,
        y: y,
        colorDifference: colorDifference,
        searchRadius: radius,
        imageId: id
    });

    $.post('/api/color/point', json, (result) => outputArea.first().text(JSON.stringify(result.pixels)))
        .fail(onfail);
}

function apiPerspective(points, base64, height, width) {
    const json = JSON.stringify({
        points: points,
        image: base64,
        outputHeight: height,
        outputWidth: width
    });
    $.post('/api/perspective', json, (result) => {
        imageIsUploaded = true;
        imageId = result.imageId;
        currentImage.attr('src', IMAGE_SRC_PREFIX + result.image);
    });
}


function loadFile(file) {
    if (file === undefined) return;
    imageIsUploaded = false;
    toBase64(file, function(data) {
        currentImage.attr('src', data);
    });
}

function toBase64(file, callback) {
    const reader = new FileReader();
    reader.onload = function() {
        callback(reader.result);
    };
    reader.readAsDataURL(file);
}

function getBase64FromImg(img) {
    return img.attr('src').split(',')[1];
}
