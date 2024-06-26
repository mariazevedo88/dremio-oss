/*
 * Copyright (C) 2017-2019 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { PureComponent } from "react";

import PropTypes from "prop-types";
import browserUtils from "utils/browserUtils";
import UnsupportedBrowserForm from "components/UnsupportedBrowserForm";
import LoginFormContainer from "./components/LoginFormContainer";

export class AuthenticationPage extends PureComponent {
  static propTypes = {
    style: PropTypes.object,
  };

  state = {
    showLoginForm:
      browserUtils.hasSupportedBrowserVersion() ||
      browserUtils.isApprovedUnsupportedBrowser(),
  };

  approveBrowser = () => {
    browserUtils.approveUnsupportedBrowser();
    this.setState({
      showLoginForm: true,
    });
  };

  render() {
    return this.state.showLoginForm ? (
      <div id="authentication-page" className="page" style={styles.base}>
        <LoginFormContainer />
      </div>
    ) : (
      <UnsupportedBrowserForm approveBrowser={this.approveBrowser} />
    );
  }
}

const styles = {
  base: {
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#2A394A",
    overflow: "hidden",
  },
};
export default AuthenticationPage;
