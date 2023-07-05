
import styled from "styled-components";
import ToggleSvg from "../../sidebar/ToggleSvg";
import SubSection from "../SubSection";
import MessageForm from "../../form/MessageForm";
import MessageSection from "../message/MessageSection";
import ChannelSectionHeader from "./ChannelSectionHeader";
import ChannelWelcome from "./ChannelWelcome";



const Wrapper = styled.div`
    color:#D1D2D3;
`;





const ChannelSectionBody = styled.div`
    height:832px;
`;
function ChannelSection(props) {
    const workspace = props.workspace;
    const currentChannel = props.channelState.currentChannel;

    return (
      <Wrapper>
          <ChannelSectionHeader workspace={workspace}/>
          <ChannelSectionBody>
              <ChannelWelcome workspace={workspace} channel={currentChannel}/>
              <MessageSection channel={currentChannel}>
              </MessageSection>
          </ChannelSectionBody>
          <MessageForm></MessageForm>
      </Wrapper>
    );
}

export default ChannelSection;