import axios from "axios";
import MyLogger from "../util/MyLogger";
import {BASE_URL} from "./ApiConstant";


export const postMessage = async (dto) => {

    MyLogger.debug("[POST] /api/messages called");
    try {
        const response = await axios.post(BASE_URL+"/api/messages",dto);
        MyLogger.debug(response.data.result);
        return response.data.result;
    } catch (err) {
        MyLogger.error(err.data.code, err.data.message);
        return null;
    }
}

export const getMessages = async (channelId) => {

    MyLogger.debug("[GET] /api/messages called");
    if(channelId == null) {
        channelId = document.location.href.split("/")[7];
    }

    try {
        const response = await axios.get(BASE_URL+"/api/messages?channelId="+channelId);
        MyLogger.debug(response.data.result);
        return response.data.result;
    } catch (err) {
        MyLogger.error(err.data.code, err.data.message);
        return null;
    }
}