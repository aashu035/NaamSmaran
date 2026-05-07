const fs = require('fs');
const path = require('path');
const sharp = require('sharp');

const ASSETS_DIR = path.join(__dirname, '..', 'Assets', 'Images');
const PUBLIC_SPLASH_DIR = path.join(__dirname, 'public', 'splash');

if (!fs.existsSync(PUBLIC_SPLASH_DIR)) {
  fs.mkdirSync(PUBLIC_SPLASH_DIR, { recursive: true });
}

async function processImages() {
  const folders = fs.readdirSync(ASSETS_DIR).filter(f => fs.statSync(path.join(ASSETS_DIR, f)).isDirectory());
  
  let count = 0;
  for (const folder of folders) {
    const folderPath = path.join(ASSETS_DIR, folder);
    const files = fs.readdirSync(folderPath).filter(f => f.match(/\.(jpg|jpeg|png)$/i));
    
    for (const file of files) {
      const inputPath = path.join(folderPath, file);
      const outputName = `splash_${count.toString().padStart(2, '0')}.jpg`;
      const outputPath = path.join(PUBLIC_SPLASH_DIR, outputName);
      
      console.log(`Processing ${folder}/${file} -> ${outputName}`);
      try {
        await sharp(inputPath)
          .resize(800, 1600, {
            fit: 'cover',
            position: 'center'
          })
          .jpeg({ quality: 80 })
          .toFile(outputPath);
        count++;
      } catch (err) {
        console.error(`Error processing ${inputPath}:`, err.message);
      }
    }
  }
  console.log(`Successfully processed ${count} images.`);
}

processImages();
