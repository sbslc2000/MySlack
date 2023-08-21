

import styled from "styled-components";

const Wrapper = styled.div`
    padding-left: ${props => props.paddingLeft}px;
    padding-right:${props => props.paddingRight}px;
    height: ${props => props.height}px;
    display:flex;
    align-items:center;
    flex-direction:row;
    border-bottom: 2px solid #2B2A2F;
    flex-shrink: 0;
`;

function SubSection(props) {

    return (
        <Wrapper paddingLeft={props.paddingLeft} paddingRight={props.paddingRight} height={props.height} >
            {props.children}
        </Wrapper>
    );
}

export default SubSection;