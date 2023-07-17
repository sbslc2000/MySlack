import {BASE_URL} from "./ApiConstant";
import axios from "axios";
import MyLogger from "../util/MyLogger";

export const getWorkspace = async (workspaceId) => {
    try {
        const response = await axios.get(BASE_URL+"/api/workspaces/"+workspaceId)
        return response.data.result;
    } catch (error) {
        MyLogger.error(error.data.code, error.data.message);
        return null;
    }
}