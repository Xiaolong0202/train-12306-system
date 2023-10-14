import {notification} from 'ant-design-vue';

const info = (type, description) => {
    notification[type]({
        message: type,
        description: description
    });
};

function generateUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0,
            v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

function isEmpty(value) {
    return !value || value.trim() === '';
}

export {
    info,
    generateUUID,
    isEmpty
}