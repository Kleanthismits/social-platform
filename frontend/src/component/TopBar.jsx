import React, { Component } from "react";
import { Link } from "react-router-dom";
import logo from "../assets/chatty-logo2.png";

class TopBar extends Component {
  render() {
    return (
      <nav>
        <div className="nav-wrapper indigo">
          <Link to="/" className="brand-logo">
            <img src={logo} width="auto" height="64px" alt="chatty logo" />
          </Link>
          <ul id="nav-mobile" className="right">
            <li>
              <Link to="/signup">Sign Up</Link>
            </li>
            <li>
              <Link to="/login">Login</Link>
            </li>
          </ul>
        </div>
      </nav>
    );
  }
}

export default TopBar;
