import MenuItem from "./ui/MenuItem";
import SmallUserIcon from "./SmallUserIcon";
import ItemList from "./ui/ItemList";
import axios from "axios";
import {useContext, useEffect, useState} from "react";
import {RefreshContext} from "../../../page/WorkspacePage";


function DirectMessageUserList(props) {
    const [isLoading,setIsLoading] = useState(true);
    const workspaceId = props.workspace.id;
    const [members,setMembers] = useState([]);

    const refreshState = useContext(RefreshContext);


    const fetchWorkspaceUsers = () => {
      axios.get("/api/workspaces/"+workspaceId+"/users").then((response)=> {
        if(response.data.isSuccess){
                console.log(response.data.result);
                setMembers(response.data.result);
            }

        setIsLoading(false);
      }).catch((error)=> {

      });
    };

    useEffect(()=>{
        fetchWorkspaceUsers();
    },[]);

    if(refreshState.directMessageRefresh) {
        console.log("directMessageRefresh");
        fetchWorkspaceUsers();
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
            <MenuItem  style={{width:260}} type="button" data-bs-toggle="modal" data-bs-target="#inviteWorkspace">
                <span style={{width: 17, display: "inline-block"}}>+</span> 직장 동료 추가
            </MenuItem>
        </ItemList>
    );
}

export default DirectMessageUserList;