export default function ResourceRow({
  resource,
  ship,
  moved,
  station,
  onChange,
}) {
  function filterInput(e) {
    const input = Math.max(0, Math.min(ship, parseInt(e.target.value)));
    return (Number.isNaN(input)) ? 0 : input;
  }
  return (
    <tr key={resource}>
      <td>
        <img
          style={{ width: "25px", height: "25px" }}
          src={"/" + resource.toLowerCase() + ".png"}
          alt={resource}
        />
      </td>
      <td>{resource}</td>
      <td>{ship - moved}</td>
      <td>
        <input
          type="number"
          min={0}
          max={ship}
          value={moved}
          onChange={(e) => onChange(resource, filterInput(e))}
        />
      </td>
      <td>{station + moved}</td>
    </tr>
  );
}
