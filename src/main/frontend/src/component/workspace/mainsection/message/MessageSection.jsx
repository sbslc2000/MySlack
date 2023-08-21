import styled from 'styled-components';
import SeparationLine from "./SeparationLine";
import DefaultUserIcon from "../../../../img/default_user.png";
import MessageWithName from "./MessageWithName";
import {useLocation} from "react-router-dom";
import axios from "axios";
import {useContext, useEffect, useState} from "react";
import MessageWithoutName from "./MessageWithoutName";
import {ChannelContext, RefreshContext} from "../../../../page/WorkspacePage";
import DateUtil from "../../../../util/DateUtil";
import {getMessages} from "../../../../api/message";

const Wrapper = styled.div`
    //min-height:470px;
    //max-height:470px;
  flex-shrink: 1;
  overflow:scroll;
`;


function MessageSection(props) {
    const channelState = useContext(ChannelContext);
    let refreshState = useContext(RefreshContext);

    let location = useLocation();
    let workspaceId = location.pathname.split("/")[3];
    let channelId = channelState.currentChannel.id;
    const [messages, setMessages] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        getMessages(channelId).then((messages) => {
            setMessages(messages);
            setIsLoading(false);
        });

        refreshState.setMessagesRefresh(false);
    }, [channelState.currentChannel,refreshState.messagesRefresh]);


    let prevSender = {};
    let prevDate = null;
    let content = <>
        {messages.map((message) => {
            let separationAddFlag = false;
            let currentDate = new Date(message.createdAt);

            if(prevDate === null || DateUtil.compareDate(prevDate,currentDate) !== 0) {
                separationAddFlag = true;
            }

            prevDate = currentDate;

            if(prevSender.id !== message.sender.id){

                prevSender = message.sender;
                return (
                <>
                    {separationAddFlag && <SeparationLine date={DateUtil.getDateKr(currentDate)}></SeparationLine>}
                    <MessageWithName message={message}/>
                </>
                );
            } else {
                prevSender = message.sender;
                return (<>
                        {separationAddFlag && <SeparationLine date={DateUtil.getDateKr(currentDate)}></SeparationLine>}
                        <MessageWithoutName message={message}/>
                </>

                );
            }
        })}
    </>;

    if(isLoading){
        content = <></>;
    }

    return (
        <Wrapper>
            {content}
        </Wrapper>
    );
}

export default MessageSection;