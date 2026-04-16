using System;
using System.Collections.Generic;
using System.Text;

namespace PvzLauncher.Android.JsonConfigs
{
    public class GameConfig
    {
        public required ImageSource Icon { get; set; }
        public required string Title { get; set; }
        public required string Notes { get; set; }
        public required string Path { get; set; }
    }

    public class DownloadConfig
    {
        public required ImageSource Icon { get; set; }
        public required string Title { get; set; }
        public required string Notes { get; set; }
        public required string Link { get; set; }
    }
}
