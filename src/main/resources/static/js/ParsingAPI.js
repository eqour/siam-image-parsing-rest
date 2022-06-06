class ParsingAPI {
    static async sendRequest(url, body) {
        return new Promise((resolve, reject) => {
            $.post(url, body, (result) => {
                resolve(result);
            })
            .fail(reject);
        });
    }
}

export default ParsingAPI;
