
import styled from "styled-components";
import ToggleSvg from "../../sidebar/ToggleSvg";
import SubSection from "../SubSection";
import MessageForm from "../../form/MessageForm";
import MessageSection from "../message/MessageSection";
import ChannelSectionHeader from "./ChannelSectionHeader";
import ChannelWelcome from "./ChannelWelcome";



const Wrapper = styled.div`
  color:#D1D2D3;
  background-color:#1A1D21;
  flex: 1;
  display:flex;
  flex-direction:column;
`;

const MessageFormSpacer = styled.div`
  height: 172px;
  flex-shrink: 0;
`;




const ChannelSectionBody = styled.div`
`;
function ChannelSection(props) {
    const workspace = props.workspace;
    const currentChannel = props.channelState.currentChannel;

    return (
      <Wrapper>
          <ChannelSectionHeader workspace={workspace}/>
          <ChannelWelcome workspace={workspace} channel={currentChannel}/>
          <MessageSection>
          </MessageSection>
          <MessageFormSpacer />
          <MessageForm></MessageForm>
      </Wrapper>
    );
}

export default ChannelSection;