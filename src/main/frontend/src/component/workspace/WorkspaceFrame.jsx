import NavbarFrame from "./navbar/NavbarFrame";
import SidebarFrame from "./sidebar/SidebarFrame";
import Sidebar from "./sidebar/Sidebar";
import MainSection from "./mainsection/MainSection";
import ChannelSection from "./mainsection/channel/ChannelSection";
import styled from "styled-components";
import ChannelSectionFrame from "./mainsection/channel/ChannelSectionFrame";

const Wrapper = styled.div`
  color:#D1D2D3;
  display: flex;
  flex-direction: column;
  height: 100%;
`;


function WorkspaceFrame(props) {
    return (
        <Wrapper>
            <NavbarFrame/>
            <div style={{display:"flex",flexGrow: 1}}>
                <SidebarFrame></SidebarFrame>
                <ChannelSectionFrame>
                    {props.children}
                </ChannelSectionFrame>
            </div>
        </Wrapper>
    )
}

export default WorkspaceFrame;