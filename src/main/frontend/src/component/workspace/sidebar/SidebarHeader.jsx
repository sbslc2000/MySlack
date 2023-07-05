import styled from "styled-components";
import ToggleSvg from "./ToggleSvg";

const Wrapper = styled.div`
    color:#D1D2D3;
    padding-left:16px;
    border-bottom: 2px solid #2B2A2F;
`;

const SidebarHeaderButtonDiv = styled.div`
    width:190px;
    height:50px;
    display:flex;
`;

const SidebarHeaderButton = styled.button`
    border:0;
    padding:4px 8px;
    background-color:#19171D;
    margin-left:-4px;
    
    &:hover {
        background-color:#27242C;
    }
    
    display:flex;
    align-items:center;
    
`;

const SidebarHeaderButtonSpan = styled.span`
    color:#D1D2D3;
    font-weight:900;
    font-size:18px;
`;

const PlanUpgradeDiv = styled.div`
    height:43px;
    display:flex;
`;

const PlanUpgrade = styled.button`
    height:30px;
    text-align:center;
    width:228px;
    border:1px solid white;
    border-radius:5px;
    line-height:27px;
    font-size:14px;
    background-color:inherit;
    color:inherit;
    
     &:hover {
        background-color:#27242C;
    }
`;
function SidebarHeader(props) {
    return (
        <Wrapper>
            <SidebarHeaderButtonDiv>
                <SidebarHeaderButton>
                    <SidebarHeaderButtonSpan>{props.name}</SidebarHeaderButtonSpan>
                    <ToggleSvg />
                </SidebarHeaderButton>
            </SidebarHeaderButtonDiv>
            <PlanUpgradeDiv className="">
                <PlanUpgrade>
                    <span className="p-upgrades_button__label" data-sk="tooltip_parent">플랜 업그레이드</span>
                </PlanUpgrade>
            </PlanUpgradeDiv>
        </Wrapper>
    );
}

export default SidebarHeader;