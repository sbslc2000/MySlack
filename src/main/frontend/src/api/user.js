import axios from "axios";
import MyLogger from "../util/MyLogger";
import {BASE_URL} from "./ApiConstant";

export const getUser = async (userId) => {

    if(userId == null) {
        userId = "me";
    }

    try {
        const response = await axios.get(BASE_URL+"/api/users/"+userId)
        return response.data.result;
    } catch (error) {
        MyLogger.error(error.data.code, error.data.message);
        return null;
    }
}

export const getUsersInWorkspace = async (workspaceId) => {
    try {
        const response = await axios.get(BASE_URL+"/api/workspaces/"+workspaceId+"/users");
        return response.data.result;
    } catch (error) {
        MyLogger.error(error.data.code, error.data.message);
        return null;
    }
}