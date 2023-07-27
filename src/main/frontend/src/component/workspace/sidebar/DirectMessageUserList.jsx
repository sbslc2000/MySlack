import MenuItem from "./ui/MenuItem";
import SmallUserIcon from "./SmallUserIcon";
import ItemList from "./ui/ItemList";
import axios from "axios";
import {useContext, useEffect, useState} from "react";
import {RefreshContext} from "../../../page/WorkspacePage";
import {getUsersInWorkspace} from "../../../api/user";
import MyLogger from "../../../util/MyLogger";


function DirectMessageUserList(props) {
    const [isLoading,setIsLoading] = useState(true);
    const workspaceId = props.workspace.id;
    const [members,setMembers] = useState([]);

    const refreshState = useContext(RefreshContext);

    useEffect(()=>{
        getUsersInWorkspace(workspaceId).then((members)=> {
            setMembers(members);
            setIsLoading(false);
        });
    },[]);

    if(refreshState.directMessageRefresh) {
        MyLogger.trace("DirectMessageRefresh");
        getUsersInWorkspace(workspaceId).then(setMembers);
        refreshState.setDirectMessageRefresh(false);
    }

    if(isLoading) {
        return <></>;
    }

    return (
        <ItemList>
            <MenuItem><span style={{fontSize:13,width:17,display:"inline-block"}}>▼</span> 다이렉트 메시지</MenuItem>
            {members.map((member) => {
                return (
                    <MenuItem key={member.id}><SmallUserIcon user={member}/> <span style={{display:"inline-block"}}>{member.nickname}</span></MenuItem>
                );
            })
            }
            <MenuItem tag={"+"} style={{width:260}} type="button" dataBsToggle="modal" dataBsTarget="#inviteWorkspace">
                직장 동료 추가
            </MenuItem>
        </ItemList>
    );
}

export default DirectMessageUserList;