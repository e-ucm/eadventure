<%--

    eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
       <e-UCM> research group.

       Copyright 2005-2010 <e-UCM> research group.

       You can access a list of all the contributors to eAdventure at:
             http://e-adventure.e-ucm.es/contributors

       <e-UCM> is a research group of the Department of Software Engineering
             and Artificial Intelligence at the Complutense University of Madrid
             (School of Computer Science).

             C Profesor Jose Garcia Santesmases sn,
             28040 Madrid (Madrid), Spain.

             For more info please visit:  <http://e-adventure.e-ucm.es> or
             <http://www.e-ucm.es>

    ****************************************************************************

     This file is part of eAdventure, version 2.0

         eAdventure is free software: you can redistribute it and/or modify
         it under the terms of the GNU Lesser General Public License as published by
         the Free Software Foundation, either version 3 of the License, or
         (at your option) any later version.

         eAdventure is distributed in the hope that it will be useful,
         but WITHOUT ANY WARRANTY; without even the implied warranty of
         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
         GNU Lesser General Public License for more details.

         You should have received a copy of the GNU Lesser General Public License
         along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.

--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=9">
<link rel="stylesheet" type="text/css" href="css/styles.css" />
<title>eAdventure Technical Demo</title>


<script type="text/javascript">
	var _gaq = _gaq || [];
	_gaq.push([ '_setAccount', 'UA-27030881-1' ]);
	_gaq.push([ '_trackPageview' ]);

	(function() {
		var ga = document.createElement('script');
		ga.type = 'text/javascript';
		ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl'
				: 'http://www')
				+ '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(ga, s);
	})();
</script>
</head>

<body>
<div id="navbar">
	<div id="menu">
	<ol>
		<li><a href="http://e-adventure.e-ucm.es/" target="_blank"><img src="css/img/logo.png" /></a>
		<li><a href="http://code.google.com/p/eadventure/" target="_blank" >Google Code</a></li>
		<li><a href="https://plus.google.com/b/105192112768097933442/105192112768097933442/posts" target="_blank">Google Plus</a><li>
	</ol>
	</div>
</div>
<div id="wrapper" class="eadbody">
	<div id="container">
		<div id="playn-root"></div>
		<div id="links">
			<a id="tech_demo_link" class="demolink" title="eAdventure Tech Demo" href="index.jsp"></a>
			<a id="fire_protocol_link" class="demolink" title="Fire Protocol Game" href="index.jsp?demo=1"></a>
		</div>
		<script src="eadengine/eadengine.nocache.js"></script>
	<div id="footer"></div>
	</div>
</div>
</body>
</html>
