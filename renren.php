<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"> 
<html  lang="zh-CN" >
 <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>Movie List</title>
    <link href="http://54.200.13.59/css/douban.css" rel="stylesheet" type="text/css">


    <style type="text/css">
    
        
.result-item { border-bottom: none; border-top: 1px dashed #ccc; margin: 0; padding: 10px 0 }
.result-item .pic { display: inline-block; zoom: 1; *display: inline; text-align: left; width: 100px }
.result-item .content { color: #666; display: inline-block; zoom: 1; *display: inline; width: 460px; vertical-align: top;}
.result-item .footer {padding-top:10px;}
.result-item em { font-style: normal; font-weight: normal }
.result-item .content h3 { background: none; margin: 0 0 5px }
.result-item .content h3 em {font-size: 12px; text-align: center; background: #71B5DE; color: #fff; display: inline-block; overflow: hidden; *display: inline; zoom: 1; border-radius: 3px; -webkit-border-radius: 3px; -moz-border-radius: 3px; padding: 0.1em 0.5em; margin-left: 5px; line-height: 16px }
.result-item .gact { text-align: left; margin-top: 10px }
.result-site .pic { width:80px; }
.content.musician ul {margin-top: 20px;}

p.first { margin-top: 0 }
.followers { margin-top:5px; }
.followers .pl { color:#999; }

a.start_radio_musician { background: url('/pics/fm/radio_8_gray-1.jpg');
                display: inline-block;
                width: 43px;
                height: 16px;
                margin-left: 16px;
                margin-bottom: -2.5px;
}

@media screen and (-webkit-min-device-pixel-ratio:0) {
    a.start_radio_musician { margin-bottom: -4px; }
}

a.start_radio { background: url('/pics/fm/radio_8_gray-1.jpg');
                display: inline-block;
                width: 43px;
                height: 16px;
                margin-left: 16px;
                margin-bottom: -2.5px;
}

.aside .entrance{
    background-color:#f2f7f6;
    padding: 10px 15px;
    font-size:14px;
    margin-bottom:15px;
    width:270px;
}

.content {
background: #fff;
position: relative;
z-index: 100000;
margin:310px 0 0 0;
padding-top: 10px;
}

#block-views-featured-block {
height: 300px;
width: 100%;
overflow: hidden;
position: fixed;
background: #f5f5f5;
left: 0;
top: 81px;
z-index: 1;
text-align: center;
}
    
.rating_nums{
color: #ff5138;
font-size: 16px;
padding: 0 5px 0 0;
}
header {
height: 80px !important;
background: #fff;
position: fixed;
display: block;
width: 100%;
top: 0 !important;
z-index: 1000000000000;
box-shadow: 0px 1px 4px rgba(0,0,0,.2);
}

#top {
position: fixed;
bottom: 0;
right: 0;
height: 34px;
width: 34px;
}

</style>
    
    <link rel="stylesheet" href="http://img3.douban.com/misc/mixed_static/62ffa9194a04453a.css">

<!-[if lt IE9]> 
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]->

 </head>
 <body>

<header>
<img src="http://54.200.13.59/pic/logo2.jpg" height="80" width="250">
</header>

<div class="region region-feature">
      <div class="block block-views" id="block-views-featured-block" style="background-image: url(http://54.200.13.59/pic/back.jpg); background-size: cover; background-position: 50% 50%; background-repeat: no-repeat no-repeat;">
 </div>
  </div>

<div id="top"><a href="#header" title="回到页眉">回到页眉</a></div>


 <div id="content" class="content">
 <div id="wrapper">
<p class="ul"></p> 
<table style="height: 30px; color: rgb(184, 91, 91);text-align: center;">
 <tr>
    <td width="80">人人影视封面</td>
    <td width="70" align="middle">豆瓣评分</td>
    <td width="150">人人影视电影名称</td>
    <td width="80">豆瓣封面</td>
    <td width="150">豆瓣电影名称</td>
    <td width="200">搜索结果</td>
 </tr>
 </table>
 <p class="ul"></p>
 <?php 
    $file_name = "/tmp/firstTowPageMovies.txt"; 
    $fp=fopen($file_name,'r');
    while(!feof($fp))
    {
        $buffer = fgets($fp,4096);
        list($score, $douNum,$douName,$douUrl,$douSearchUrl,$renName,$renUrl,$renImgUrl,$douImgUrl) = split ("\t", $buffer, 9);
        if($renName == "") continue;
        //echo "<tr class=\"item\"><td>".$buffer."</td></tr>";
?>
    <table style="text-align: center;">
        <tr>
            <td width="80" valign="midlle"><img src="<?php echo $renImgUrl ?>" width="70" height="95"></td>
            <td width="70" valign="middle" align="middle" class="rating_nums"><?php echo $score ?> </td>
            <td width="150" valign="middle"><a target="_blank" href="<?php echo $renUrl ?>" ><strong><?php echo $renName ?></strong></a></td>
            <td width="80" valign="middle"><img src="<?php echo $douImgUrl ?>"></td>
            <td width="150" valign="middle"><a target="_blank" href="<?php echo $douUrl ?>" ><strong><?php echo $douName ?></strong></a></td>
            <td width="200" valign="middle"><a target="_blank" href="<?php echo $douSearchUrl ?>" >搜索结果</a></td>
        </tr>
    </table>
<p class="ul"></p>
<?php       
    }
    fclose($fp);
?> 
</div> </div>
 </body>
</html>
