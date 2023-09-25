import { notification } from 'ant-design-vue';
const info = (type, description) => {
    notification[type]({
        message: type,
        description: description
    });
};

export {
    info
}