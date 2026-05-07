export function getSpiritualDate(date = new Date(), boundaryHour = 3) {
  const d = new Date(date);
  
  if (d.getHours() < boundaryHour) {
    d.setDate(d.getDate() - 1);
  }
  
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  
  return `${year}-${month}-${day}`;
}
