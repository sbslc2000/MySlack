import NavbarFrame from "./navbar/NavbarFrame";
import SidebarFrame from "./sidebar/SidebarFrame";
import Sidebar from "./sidebar/Sidebar";
import MainSection from "./mainsection/MainSection";
import ChannelSection from "./mainsection/channel/ChannelSection";
import styled from "styled-components";

const Wrapper = styled.div`
    color:#D1D2D3;
`;


function WorkspaceFrame(props) {
    return (
        <Wrapper>
            <NavbarFrame></NavbarFrame>

            <div style={{display:"flex"}}>
                <SidebarFrame></SidebarFrame>
                <MainSection>
                    {props.children}
                </MainSection>
                <MainSection/>
            </div>
        </Wrapper>
    )
}

export default WorkspaceFrame;