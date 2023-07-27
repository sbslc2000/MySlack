import axios from "axios";
import MyLogger from "../util/MyLogger";
import {BASE_URL} from "./ApiConstant";


export const getChannelsByWorkspaceId = async (workspaceId) => {

    MyLogger.debug("[GET] /api/workspaces/" + workspaceId + "/channels called");
    if(workspaceId == null) {
        console.log(document.location.href);
        workspaceId = document.location.href.split("/")[5];
    }

    try {
        const response = await axios.get(BASE_URL+"/api/workspaces/" + workspaceId + "/channels")
        MyLogger.debug(response.data.result);
        return response.data.result;
    } catch (err) {
        MyLogger.error(err.data.code, err.data.message);
        return null;
    }
}