import SidebarFrame from "./SidebarFrame";
import SidebarHeader from "./SidebarHeader";
import WorkspaceSearch from "./WorkspaceSearch";

function Sidebar(props) {

    return (
        <SidebarFrame>
            <SidebarHeader name={props.workspace.name}/>
            <WorkspaceSearch channelState={props.channelState} workspace={props.workspace}/>
        </SidebarFrame>
    );
}

export default Sidebar;