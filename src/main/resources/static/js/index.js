var currentImage = $('#current-image');
var imageInput = $('#image-input');
var colorSelector = $('#color-select');
var colorDifferenceSelector = $('#color-difference');
var colorSearchRadiusSelector = $('#color-search-radius');
var colorEntireButton = $('#color-entire-submit');
var colorAreaButton = $('#color-area-submit');
var colorPointButton = $('#color-point-submit');
var perspectiveButton = $('#perspective-submit');
var perspectiveWidthSelector = $('#perspective-width');
var perspectiveHeightSelector = $('#perspective-height');
var smoothButton = $('#smooth-submit');
var smoothIterationsSelector = $('#smooth-iterations');
var outputArea = $('#output');
var convertAreaButton = $('#convert-area-submit');
var inputX1 = $('#x1');
var inputX2 = $('#x2');
var inputY1 = $('#y1');
var inputY2 = $('#y2');

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
smoothButton.click(smoothHandler);
convertAreaButton.click(convertAreaHandler);

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
    const width = perspectiveWidthSelector.first().val();
    const height = perspectiveHeightSelector.first().val();
    apiPerspective(selectedPoints, getBase64FromImg(currentImage), height, width);
}

function smoothHandler() {
    const points = JSON.parse(outputArea.first().text());
    const iterations = smoothIterationsSelector.first().val();
    apiSmooth(points, iterations);
}

function convertAreaHandler() {
    const l = selectedPoints.length;
    if (l < 2) return;

    const points = JSON.parse(outputArea.first().text());
    apiConvertArea(
        points,
        selectedPoints[l - 2][0],
        selectedPoints[l - 1][0],
        selectedPoints[l - 2][1],
        selectedPoints[l - 1][1],
        parseInt(inputX1.val()),
        parseInt(inputX2.val()),
        parseInt(inputY1.val()),
        parseInt(inputY2.val())
    );
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

function apiSmooth(points, maxIteration) {
    const json = JSON.stringify({
        points: points,
        maxIteration: maxIteration
    });
    $.post('/api/smooth', json, (result) => {
        outputArea.first().text(JSON.stringify(result.points));
    });
}

function apiConvertArea(points, sx1, sx2, sy1, sy2, ex1, ex2, ey1, ey2) {
    const json = JSON.stringify({
        points: points,
        srcSX: sx1,
        srcSY: sy1,
        srcEX: sx2,
        srcEY: sy2,
        dstSX: ex1,
        dstSY: ey1,
        dstEX: ex2,
        dstEY: ey2
    });
    $.post('/api/convert/area', json, (result) => {
        outputArea.first().text(JSON.stringify(result.points));
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
