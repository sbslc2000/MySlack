
import styled from "styled-components";
const Wrapper = styled.svg`
    width:18px;
    height:18px;
    color:#D1D2D3;
`;
function ToggleSvg(props) {

    return (
        <Wrapper data-nwn="true" aria-hidden="true" viewBox="0 0 20 20" className="">
            <path fill="currentColor" fill-rule="evenodd"
                  d="M5.72 7.47a.75.75 0 0 1 1.06 0L10 10.69l3.22-3.22a.75.75 0 1 1 1.06 1.06l-3.75 3.75a.75.75 0 0 1-1.06 0L5.72 8.53a.75.75 0 0 1 0-1.06Z"
                  clip-rule="evenodd"></path>
        </Wrapper>
    );
}

export default ToggleSvg;