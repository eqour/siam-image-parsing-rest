class FileHelper {
    static parseDataURL(dataURL) {
        const splitted = dataURL.split(',');
        return {
            prefix: splitted[0] + ',',
            data: splitted[1],
            full: dataURL
        };
    }

    static async fileToBase64Async(file) {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = () => resolve(reader.result);
            reader.onerror = (e) => reject(e);
            reader.readAsDataURL(file);
        });
    }
}

export default FileHelper;
