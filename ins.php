<?php
$con = mysql_connect("mysql8.000webhost.com","a8685307_naga","hari1000");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }
mysql_select_db("a8685307_vdb", $con);
$sql1="TRUNCATE TABLE vals";
if (mysql_query($sql1,$con))
  {
  	echo "Value deleted";
  }
else
  {
  echo "Error creating database: " . mysql_error();
  }
$val2=$_POST['value'];
$sq2 = "INSERT INTO vals VALUES ('$val2')";
if (mysql_query($sq2,$con))
  {
  	echo "Value inserted";
  }
else
  {
  echo "Error creating database: " . mysql_error();
  }
?>
