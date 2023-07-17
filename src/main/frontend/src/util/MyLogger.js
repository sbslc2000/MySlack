

class MyLogger {

    static level = 0;

    static trace(message) {
        if(this.level <= 0) {
            console.log(message);
        }
    }

    static info(message) {
        if(this.level <= 1) {
            console.log(message);
        }
    }

    static data(description,typeName,data) {
        if(this.level <= 1) {
            console.log(description);
            if(typeof data === "object") {
                console.log(typeName + ":" + JSON.stringify(data));
            }
            else {
                console.log(typeName + ":" + data);
            }
        }
    }
}

export default MyLogger;