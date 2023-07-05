import styled from "styled-components";
import SidebarHeader from "./SidebarHeader";
import WorkspaceSearch from "./WorkspaceSearch";
import SidebarFrame from "./SidebarFrame";
import {useEffect, useState} from "react";
import axios from "axios";

const Wrapper = styled.div`
    width:260px;
    background-color:#19171D;
    height:917px;
    overflow:scroll;
`;


function Sidebar(props) {


    let content;


    content = <SidebarFrame>
        <SidebarHeader name={props.workspace.name}/>
        <WorkspaceSearch channelState={props.channelState} workspace={props.workspace}/>
    </SidebarFrame>



    return (
        <div>
            {content}
        </div>
    );
}

export default Sidebar;