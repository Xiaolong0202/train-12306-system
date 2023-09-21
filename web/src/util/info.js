import { notification } from 'ant-design-vue';
const openNotificationWithIcon = (type,description) => {
    notification[type]({
        message: type,
        description: description
    });
};

export {
    openNotificationWithIcon
}