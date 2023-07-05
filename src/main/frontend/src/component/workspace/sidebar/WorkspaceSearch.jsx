import styled from "styled-components";
import SmallUserIcon from "./SmallUserIcon";
import {useState} from "react";
import ItemList from "./ui/ItemList";
import MenuItem from "./ui/MenuItem";
import DirectMessageUserList from "./DirectMessageUserList";
import CreateChannelModal from "../createchannel/CreateChannelModal";

const Wrapper = styled.div`
    color : #9A9A9D;
`;


const ChannelAddDropdownMenu = styled.ul`
    background-color: #222529;
    width: 200px;
    height:80px;
    border-radius:5px;
    position:relative;
    right:30px;
 
`;

const ChannelAddDropdownList = styled.li`

    background-color:inherit;
   
    > button:hover{ 
        background-color:#1264A3;
        color:white;
    }
    
    > button {
        background-color:inherit;
        border:0;
        color:#D1D2D3; 
    }
        

`;

function WorkspaceSearch(props) {


    const channels = props.channelState.channels;
    const currentChannel = props.channelState.currentChannel;



    const handleChangeChannel = (e) => {

        const targetValue = e.target.id;
        console.log(targetValue);
        props.channelState.setCurrentChannel(channels.find((channel) => {
            return channel.id == targetValue;
        }));
    }

    return (
        <Wrapper>
            <ItemList>
                <MenuItem><span style={{width: 17, display: "inline-block"}}>#</span> 스레드</MenuItem>
                <MenuItem><span style={{width: 17, display: "inline-block"}}>@</span> 멘션 및 반응</MenuItem>
                <MenuItem><span style={{width: 17, display: "inline-block"}}>!</span> 초안 및 전송됨</MenuItem>
                <MenuItem><span style={{width: 17, display: "inline-block"}}>?</span> MySlack Connect</MenuItem>
            </ItemList>
            <ItemList>
                <MenuItem><span style={{fontSize: 13, width: 17, display: "inline-block"}}>▼</span> 채널</MenuItem>
                {channels.map((channel) => {
                    return (
                        <MenuItem selected={channel.id == props.channelState.currentChannel.id}
                                  onClick={handleChangeChannel} id={channel.id} key={channel.id}><span
                            style={{width: 17, display: "inline-block"}}>#</span> {channel.name}</MenuItem>
                    );
                })
                }
                <div className={"dropdown"}>
                    <MenuItem style={{width:260}} type="button" data-bs-toggle="dropdown">
                        <span style={{width: 17, display: "inline-block"}}>+</span> 채널 추가
                    </MenuItem>
                    <ChannelAddDropdownMenu className="dropdown-menu">
                        <ChannelAddDropdownList><button className="dropdown-item" data-bs-toggle="modal" data-bs-target="#createChannel" >새 채널 생성</button></ChannelAddDropdownList>
                        <ChannelAddDropdownList><button className="dropdown-item" href="#">채널 탐색</button></ChannelAddDropdownList>

                    </ChannelAddDropdownMenu>
                </div>
            </ItemList>
            <DirectMessageUserList workspace={props.workspace}></DirectMessageUserList>
        </Wrapper>

    )
        ;

};


export default WorkspaceSearch;