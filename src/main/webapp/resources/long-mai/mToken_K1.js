function mToken(obj){
	this.obj = obj;	
	
	var g_mTokenPlugin = null;

	this.LoadLibrary = function()
	{
		g_mTokenPlugin = document.getElementById(obj);
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return 0;
	};

	this.K1_mTokenGetVersion = function()
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.mTokenGetVersion();
	};

	this.K1_mTokenFindDevice = function()
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.mTokenFindDevice();
	};

	this.K1_mTokenGetLastError = function()
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.mTokenGetLastError();
	};

	this.K1_mTokenGetUID = function(keyIndex)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.mTokenGetUID(keyIndex);
	};

	this.K1_mTokenOpen = function(keyUID, keyPassword)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.mTokenOpen(keyUID, keyPassword, 1);
	};

	this.K1_mTokenClose = function()
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.mTokenClose();
	};

	this.K1_mTokenChangePwd = function(keyUID,oldPassword, newPassword)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.mTokenChangePwd(keyUID, 1, oldPassword, newPassword);
	};

	this.K1_mTokenSHA1WithSeed = function(keyUID, randomStr)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.mTokenSHA1WithSeed(keyUID, randomStr);
	};

	this.K1_mTokenGenResetPwdRequest = function(keyUID, userInfo)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.mTokenGenResetPwdRequest(keyUID, userInfo);
	};

	this.K1_mTokenResetPassword = function(keyUID, serverResponse)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.mTokenResetPassword(keyUID, serverResponse);
	};
	
	this.K1_mTokenGenRandom = function(keyUID, randomLength)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.mTokenGenRandom(keyUID, randomLength);
	};

	this.K1_mTokenReadSecureStorage = function(keyUID, offset, dataLength)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.mTokenReadSecureStorage(keyUID, offset, dataLength);
	};

	this.K1_mTokenWriteSecureStorag = function(keyUID, offset, writeData)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.mTokenWriteSecureStorage(keyUID, offset, writeData);
	};

	this.K1_mTokenReadUserStorage = function(keyUID, offset, dataLength)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.mTokenReadUserStorage(keyUID, offset, dataLength);
	};

	this.K1_mTokenWriteUserStorage = function(keyUID, offset, writeData)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.mTokenWriteUserStorage(keyUID, offset, writeData);
	};

	this.K1_mTokenGetURL = function(keyUID)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.mTokenGetURL(keyUID);
	};

	this.K1_mTokenGetLabel = function(keyUID)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.mTokenGetLabel(keyUID);
	};

	this.K1_mTokenGetCompanyName = function(keyUID)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.mTokenGetCompanyName(keyUID);
	};

	this.K1_mTokenGetRemark = function(keyUID)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.mTokenGetRemarks(keyUID);
	};
	this.K1_mTokenGetOpenType = function(keyUID)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.mTokenGetOpenType(keyUID);
	};	
}

