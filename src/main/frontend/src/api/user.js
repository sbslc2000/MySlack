import axios from "axios";
import MyLogger from "../util/MyLogger";
import {BASE_URL} from "./ApiConstant";

export const getUser = async (userId) => {

    MyLogger.debug("[GET] /api/users/"+userId+" called");

    if(userId == null) {
        userId = "me";
    }

    try {
        const response = await axios.get(BASE_URL+"/api/users/"+userId);
        MyLogger.debug(response.data.result);
        return response.data.result;
    } catch (error) {
        MyLogger.error(error.data.code, error.data.message);
        return null;
    }
}

export const getUsersInWorkspace = async (workspaceId) => {

    MyLogger.debug("[GET] /api/workspaces/"+workspaceId+"/users called");
    try {
        const response = await axios.get(BASE_URL+"/api/workspaces/"+workspaceId+"/users");
        MyLogger.debug(response.data.result);
        return response.data.result;
    } catch (error) {
        MyLogger.error(error.data.code, error.data.message);
        return null;
    }
}

export const getMyUser = async () => {
    MyLogger.debug("[GET] /api/users/me called");

    try {
        const response = await axios.get(BASE_URL+"/api/users/me");
        MyLogger.debug(response.data.result);
        return response.data.result;
    } catch (error) {
        MyLogger.error(error.data.code, error.data.message);
        return null;
    }
}