import SubSection from "../SubSection";
import ToggleSvg from "../../sidebar/ToggleSvg";
import styled from "styled-components";



const ChannelSectionHeaderTitle = styled.button`
    background-color:inherit;
    color:inherit;
    border:0;
    font-size:18px;
    font-weight:900;
    
    height:30px;
    line-height:30px;
    
    &:hover {
        background-color:#222529;
    }
`;

const BookmarkButton = styled.button`
    background-color:inherit;
    color:#ABABAD;
    border:0;
    font-size:12px;
    font-weight:700;
`;
function ChannelSectionHeader(props) {

    const workspace = props.workspace;

    return (
        <>
            <SubSection title={"ChannelSectionHeader"} paddingLeft={20} paddingRight={16} height={49} >
                <ChannelSectionHeaderTitle>
                    <span>{workspace.name}</span>
                    <ToggleSvg></ToggleSvg>
                </ChannelSectionHeaderTitle>
            </SubSection>
            <SubSection title={"Bookmark"} paddingLeft={20} paddingRight={20} height={36}>
                <BookmarkButton>+ 책갈피 추가</BookmarkButton>
            </SubSection>
    </>
    );
}

export default ChannelSectionHeader;