var currentImage = $('#current-image');
var imageInput = $('#image-input');
var colorSelector = $('#color-select');
var colorDifferenceSelector = $('#color-difference');
var colorSearchButton = $('#color-submit');
var outputArea = $('#output');

var imageId = 0;

$.ajaxSetup({
    contentType: 'application/json; charset=UTF-8'
});

imageInput.change((e) => loadFile(e.target.files[0]));
colorSearchButton.click(() => apiColorEntire(colorSelector, colorDifferenceSelector, imageId));

function loadFile(file) {
    if (file === undefined) return;
    imageId = 0;
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

function apiImage(imageData, oncomplete) {
    const json = JSON.stringify({ image: imageData.split(',')[1] });
    $.post('/api/image', json, (result) => {
        imageId = result.imageId;
        oncomplete();
    });
}

function apiColorEntire(color, colorDifference, id) {
    const json = JSON.stringify({
        color: {
            red: parseInt(color.val().substr(1, 2), 16),
            green: parseInt(color.val().substr(3, 2), 16),
            blue: parseInt(color.val().substr(5, 2), 16),
        },
        colorDifference: parseInt(colorDifference.val()),
        imageId: id
    });
    $.post('/api/color/entire', json, (result) => outputArea.first().text(JSON.stringify(result.pixels)))
    .fail(function(xhr) {
        if (xhr === undefined || xhr.responseJSON === undefined || xhr.responseJSON.message === undefined) return;
        if (xhr.responseJSON.message.indexOf('Изображение отсутствует в кэше') !== -1) {
            apiImage(currentImage.attr('src'), () => apiColorEntire(color, colorDifference, imageId));
        }
    });
}
