import axios from "axios";
import MyLogger from "../util/MyLogger";
import {BASE_URL} from "./ApiConstant";


export const postMessage = async (dto,workspaceId,channelId) => {

    if(workspaceId == null) {
        workspaceId = document.location.href.split("/")[5];
    }

    if(channelId == null) {
        channelId = document.location.href.split("/")[7];
    }

    try {
        const response = await axios.post(BASE_URL+"/api/workspaces/" + workspaceId + "/channels/"+channelId+"/messages",dto)
        return response.data.result;
    } catch (err) {
        MyLogger.error(err.data.code, err.data.message);
        return null;
    }
}

export const getMessages = async (workspaceId,channelId) => {
    if(workspaceId == null) {
        workspaceId = document.location.href.split("/")[5];
    }

    if(channelId == null) {
        channelId = document.location.href.split("/")[7];
    }

    try {
        const response = await axios.get(BASE_URL+"/api/workspaces/" + workspaceId + "/channels/"+channelId+"/messages")
        return response.data.result;
    } catch (err) {
        MyLogger.error(err.data.code, err.data.message);
        return null;
    }
}